import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Show} from '../dtos/show';
import {Booked} from '../dtos/booked';

@Injectable({
  providedIn: 'root'
})
export class ShowService {
  private messageBaseUri: string = this.globals.backendUri + '/shows';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  public static showInFuture(show: Show): boolean {
    const now = new Date();

    if(new Date(show.date) === now){
      if(show.hour === now.getHours()){
        return show.minute > now.getMinutes();
      }
      return show.hour > now.getHours();
    }
    return new Date(show.date) > now;
  }

  public static sortShows(a: Show, b: Show): number {
    const d1 = new Date(a.date).getTime()/1000 + (a.hour*60 + a.minute)*60;
    const d2 = new Date(b.date).getTime()/1000 + (b.hour*60 + b.minute)*60;

    if(d1-d2!==0){
      return d1-d2;
    }

    if(!a.hallplan || !b.hallplan){
      return -1;
    }

    return a.hallplan.name.localeCompare(b.hallplan.name);
  }

  /**
   * Loads booked seats/areas from backend
   */
  getBooked(id: number): Observable<Booked> {
    return this.httpClient.get<Booked>(this.messageBaseUri+'/'+id+'/booked');
  }

  /**
   * Loads all shows from the backend
   */
  getAll(): Observable<Show[]> {
    return this.httpClient.get<Show[]>(this.messageBaseUri);
  }

  /**
   * Loads specific show from the backend
   *
   * @param id of show to load
   */
  getById(id: number): Observable<Show> {
    console.log('Load show details for ' + id);
    return this.httpClient.get<Show>(this.messageBaseUri + '/' + id);
  }

  /**
   * Persists show to the backend
   *
   * @param show to persist
   */
  save(eventId: number, show: Show): Observable<Show> {
    console.log('Create show', show);
    return this.httpClient.post<Show>(this.messageBaseUri+'/'+eventId, show);
  }

  /**
   * updates show in the backend
   *
   * @param show to update
   */
  edit(show: Show): Observable<Show> {
    console.log('edit show', show);
    return this.httpClient.put<Show>(this.messageBaseUri, show);
  }
}
