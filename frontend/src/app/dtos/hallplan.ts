import {HallplanSeat} from './hallplan-seat';
import {HallplanArea} from './hallplan-area';
import {HallplanCat} from './hallplan-cat';
import {Loc} from './loc';

export interface Hallplan {
  /** id in database */
  id: number;
  name: string;
  location: Loc;

  /** list of prices */
  cats: HallplanCat[];

  /** list of seats */
  seats: HallplanSeat[];
  /** list of areas */
  areas: HallplanArea[];
}
