import {Even} from './even';
import {Hallplan} from './hallplan';

export interface Show {
  id: number;
  date: string;
  hour: number;
  minute: number;
  event: Even;
  hallplan: Hallplan;
}
