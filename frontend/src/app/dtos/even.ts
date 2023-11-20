import {Show} from './show';

export interface Even {
  id: number;
  name: string;
  duration: number;
  description: string;
  shows: Show[];
  artists: string;
  category: EvenCategory;
  image?: boolean;
}

export interface EvenCategory {
  category: string;
}
