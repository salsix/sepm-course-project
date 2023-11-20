import {Hallplan} from './hallplan';

export interface Loc {
  id: number;
  name: string;
  country: string;
  city: string;
  zip: string;
  street: string;
  hallplans: Hallplan[];
}
