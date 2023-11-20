import {HallplanData} from './hallplan-data';
import {HallplanPosition} from './hallplan-position';

export interface HallplanArea extends HallplanData {
  //polygon points
  positions: HallplanPosition[];

  //max booked spaces in this area
  count: number;

  polyString?: string;
  x?: number;
  y?: number;
}
