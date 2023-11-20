import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Loc} from '../dtos/loc';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private messageBaseUri: string = this.globals.backendUri + '/locations';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all locations from the backend
   */
  getAll(): Observable<Loc[]> {
    return this.httpClient.get<Loc[]>(this.messageBaseUri);
  }

  /**
   * Loads a location from the backend
   */
  get(id: number): Observable<Loc> {
    return this.httpClient.get<Loc>(this.messageBaseUri+'/'+id);
  }

  /**
   * save a location
   */
  save(location: Loc): Observable<any> {
    console.log(this.messageBaseUri);
    return this.httpClient.post<Loc>(this.messageBaseUri, location);
  }

  /**
   * update a location
   */
  edit(location: Loc): Observable<any> {
    console.log(this.messageBaseUri);
    return this.httpClient.put<Loc>(this.messageBaseUri, location);
  }

  listCountries(): Observable<string[]> {
    return this.httpClient.get<string[]>(this.messageBaseUri+'/countries');
  }

  listZips(country: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.messageBaseUri+'/countries/'+country+'/zips');
  }

  listLocations(country: string, zip: string): Observable<Loc[]> {
    return this.httpClient.get<Loc[]>(this.messageBaseUri+'/countries/'+country+'/zips/'+zip+'/locations');
  }
}
