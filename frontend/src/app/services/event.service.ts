import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Even} from '../dtos/even';
import {TopTen} from '../dtos/top-ten';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private messageBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all events from the backend
   */
  getAll(): Observable<Even[]> {
    return this.httpClient.get<Even[]>(this.messageBaseUri);
  }

  /**
   * Loads specific event from the backend
   *
   * @param id of event to load
   */
  getById(id: number): Observable<Even> {
    console.log('Load event details for ' + id);
    return this.httpClient.get<Even>(this.messageBaseUri + '/' + id);
  }

  /**
   * Persists event to the backend
   *
   * @param event to persist
   */
  save(event: Even): Observable<Even> {
    console.log('Create event', event);
    return this.httpClient.post<Even>(this.messageBaseUri, event);
  }

  /**
   * updates event in the backend
   *
   * @param event to update
   */
  edit(event: Even): Observable<Even> {
    console.log('edit event', event);
    return this.httpClient.put<Even>(this.messageBaseUri, event);
  }

  image(eventId: number, image: File): Observable<any> {
    console.log('image', eventId, image);
    const formData = new FormData();
    formData.append('image', image, image.name);
    return this.httpClient.post<any>(this.imagePath(eventId), formData);
  }

  imagePath(eventId: number): string {
    return this.messageBaseUri+'/'+eventId+'/image';
  }

  addCategory(category: string): Observable<any>{
    console.log('add category');
    return this.httpClient.post<any>(this.messageBaseUri + '/categories', category);
  }

  getCategories(): Observable<string[]>{
    return this.httpClient.get<string[]>(this.messageBaseUri + '/categories');
  }

  topten(category: string): Observable<TopTen[]> {
    console.log('topten', category);
    return this.httpClient.get<TopTen[]>(this.messageBaseUri+'/topTen?category='+category);
  }
}
