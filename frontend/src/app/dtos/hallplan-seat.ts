import {HallplanData} from './hallplan-data';

export interface HallplanSeat extends HallplanData {
  //image of seat
  image: string;

  //position of first seat
  positionX: number;
  positionY: number;

  //offset to second seat
  offsetX: number;
  offsetY: number;

  //start number of first seat
  startNumber: number;
}
