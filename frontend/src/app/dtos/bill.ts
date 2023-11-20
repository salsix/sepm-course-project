import {Show} from './show';
import {Booked} from './booked';

export interface Bill {
  id: number;
  price: number;
  buyTime: string;
  stornoTime?: string;

  want: Booked;
  booked: Booked;

  reserved: boolean;
  storno: boolean;

  show: Show;
}
