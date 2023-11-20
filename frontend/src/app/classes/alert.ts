
export interface Alert {
  id?: number;
  type: string;
  strongMessage: string;
  message: string;
  duration?: number;

  state?: string;
}
