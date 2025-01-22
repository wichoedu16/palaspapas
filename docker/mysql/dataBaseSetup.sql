CREATE TABLE roles (
                       id LONG PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       description VARCHAR(200),
                       status BOOLEAN DEFAULT true,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP
);

CREATE TABLE permissions (
                             id LONG PRIMARY KEY,
                             code VARCHAR(50) NOT NULL UNIQUE,
                             name VARCHAR(100) NOT NULL,
                             description VARCHAR(200),
                             status BOOLEAN DEFAULT true,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP
);

CREATE TABLE role_permissions (
                                  role_id BIGINT REFERENCES roles(id),
                                  permission_id BIGINT REFERENCES permissions(id),
                                  PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users (
                       id LONG PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       email VARCHAR(100),
                       role_id BIGINT REFERENCES roles(id),
                       status BOOLEAN DEFAULT true,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP
);

CREATE TABLE user_sessions (
                               id LONG PRIMARY KEY,
                               user_id BIGINT REFERENCES users(id),
                               login_date TIMESTAMP NOT NULL,
                               logout_date TIMESTAMP,
                               ip_address VARCHAR(50),
                               device_info VARCHAR(200)
);

## Tablas de Negocio

CREATE TABLE categories (
                            id LONG PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description VARCHAR(200),
                            status BOOLEAN DEFAULT true,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP
);

CREATE TABLE ingredients (
                             id LONG PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             description VARCHAR(200),
                             category_id BIGINT REFERENCES categories(id),
                             unit_measure VARCHAR(20) NOT NULL,
                             stock DECIMAL(10,2) NOT NULL,
                             minimum_stock DECIMAL(10,2) NOT NULL,
                             is_additional BOOLEAN DEFAULT false,
                             is_kitchen BOOLEAN DEFAULT true,
                             cost DECIMAL(10,2) NOT NULL,
                             status BOOLEAN DEFAULT true,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP
);

CREATE TABLE products (
                          id LONG PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(200),
                          category_id BIGINT REFERENCES categories(id),
                          price DECIMAL(10,2) NOT NULL,
                          status BOOLEAN DEFAULT true,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP
);

CREATE TABLE product_ingredients (
                                     id LONG PRIMARY KEY,
                                     product_id BIGINT REFERENCES products(id),
                                     ingredient_id BIGINT REFERENCES ingredients(id),
                                     quantity DECIMAL(10,2) NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP
);

CREATE TABLE suppliers (
                           id LONG PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           document_number VARCHAR(20),
                           contact_person VARCHAR(100),
                           phone VARCHAR(20),
                           email VARCHAR(100),
                           status BOOLEAN DEFAULT true,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP
);

CREATE TABLE supplier_payments (
                                   id LONG PRIMARY KEY,
                                   supplier_id BIGINT REFERENCES suppliers(id),
                                   amount DECIMAL(10,2) NOT NULL,
                                   payment_date TIMESTAMP NOT NULL,
                                   document_number VARCHAR(50),
                                   reference_number VARCHAR(50),
                                   payment_type VARCHAR(20) NOT NULL,
                                   notes TEXT,
                                   status VARCHAR(20) NOT NULL, -- PENDING, PARTIAL, COMPLETED
                                   created_by BIGINT REFERENCES users(id),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP
);

CREATE TABLE sales (
                       id LONG PRIMARY KEY,
                       sale_date TIMESTAMP NOT NULL,
                       total_amount DECIMAL(10,2) NOT NULL,
                       discount_amount DECIMAL(10,2) DEFAULT 0,
                       final_amount DECIMAL(10,2) NOT NULL,
                       status VARCHAR(20) NOT NULL, -- PENDING, COMPLETED, CANCELLED
                       payment_type VARCHAR(20) NOT NULL,
                       notes TEXT,
                       created_by BIGINT REFERENCES users(id),
                       approved_by BIGINT REFERENCES users(id),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP
);

CREATE TABLE sale_details (
                              id LONG PRIMARY KEY,
                              sale_id BIGINT REFERENCES sales(id),
                              product_id BIGINT REFERENCES products(id),
                              quantity DECIMAL(10,2) NOT NULL,
                              unit_price DECIMAL(10,2) NOT NULL,
                              subtotal DECIMAL(10,2) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sale_detail_additionals (
                                         id LONG PRIMARY KEY,
                                         sale_detail_id BIGINT REFERENCES sale_details(id),
                                         ingredient_id BIGINT REFERENCES ingredients(id),
                                         quantity DECIMAL(10,2) NOT NULL,
                                         unit_price DECIMAL(10,2) NOT NULL,
                                         subtotal DECIMAL(10,2) NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE daily_cash (
                            id LONG PRIMARY KEY,
                            date DATE NOT NULL UNIQUE,
                            opening_amount DECIMAL(10,2) NOT NULL,
                            closing_amount DECIMAL(10,2),
                            difference DECIMAL(10,2),
                            status VARCHAR(20) NOT NULL, -- OPEN, CLOSED
                            notes TEXT,
                            opened_by BIGINT REFERENCES users(id),
                            closed_by BIGINT REFERENCES users(id),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP
);

CREATE TABLE inventory_movements (
                                     id LONG PRIMARY KEY,
                                     ingredient_id BIGINT REFERENCES ingredients(id),
                                     movement_type VARCHAR(20) NOT NULL, -- IN, OUT
                                     quantity DECIMAL(10,2) NOT NULL,
                                     reference_type VARCHAR(20) NOT NULL, -- SALE, PURCHASE, ADJUSTMENT
                                     reference_id BIGINT,
                                     notes TEXT,
                                     created_by BIGINT REFERENCES users(id),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);