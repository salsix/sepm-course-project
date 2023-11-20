export interface HallplanData {
  cat: number;
  count: number;

  //identifier - row / parterre / ...
  identifier: string;
  //identifier number
  rowNumber: number;

  select?: boolean;
}
