export interface ApplicationUser{
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  gender: string;
  dateOfBirth: string;
  admin: boolean;
  locked: boolean;
  loginFails: number;
}
