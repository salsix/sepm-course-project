import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Hallplan} from '../dtos/hallplan';
import {HallplanArea} from '../dtos/hallplan-area';

@Injectable({
  providedIn: 'root'
})
export class HallplanService {
  private messageBaseUri: string = this.globals.backendUri + '/hallplans';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  public static updateArea(area: HallplanArea) {
    const length = area.positions.length;
    const str: string[] = [];
    let x = 0;
    let y = 0;
    for (let i = 0; i < length; ++i) {
      str.push(area.positions[i].x + ',' + area.positions[i].y);
      x += area.positions[i].x;
      y += area.positions[i].y;
    }
    area.polyString = str.join(' ');
    area.x = x / length;
    area.y = y / length;
  }

  /**
   * Loads a location from the backend
   */
  get(id: number): Observable<Hallplan> {
    console.log('get', id);
    return this.httpClient.get<Hallplan>(this.messageBaseUri + '/' + id);
  }

  /**
   * save a hallplan belonging to location with id
   */
  save(id: number, hallplan: Hallplan): Observable<Hallplan> {
    console.log('save', hallplan);
    return this.httpClient.post<Hallplan>(this.messageBaseUri + '/' + id, hallplan);
  }

  /**
   * update a location
   */
  edit(hallplan: Hallplan): Observable<Hallplan> {
    console.log('edit', hallplan);
    return this.httpClient.put<Hallplan>(this.messageBaseUri, hallplan);
  }

  /**
   * update a location
   */
  delete(id: number): Observable<void> {
    console.log('delete', id);
    return this.httpClient.delete<void>(this.messageBaseUri + '/' + id);
  }

}
