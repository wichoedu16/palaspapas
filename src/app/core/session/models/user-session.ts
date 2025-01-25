export interface UserSession {
    id: number;
    userId: number;
    loginDate: Date;
    logoutDate?: Date;
    ipAddress: string;
    deviceInfo: string;
  }

  export interface DeviceInfo {
    userAgent: string;
    platform: string;
    language: string;
    screenResolution: string;
    timeZone: string;
  }
  