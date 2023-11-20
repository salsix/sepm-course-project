import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Even} from '../dtos/even';
import {FormGroup} from '@angular/forms';
import {Show} from '../dtos/show';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private eventSearchUri: string = this.globals.backendUri + '/events/find';
  private showSearchUri: string = this.globals.backendUri + '/shows/find';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all events from the backend
   */
  eventSearch(searchTerms: string[], page: number): Observable<Even[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    for (let i = 1; i <= searchTerms.length ; i++) {
      params = params.set('s'+ i, searchTerms[i-1]);
    }
    return this.httpClient.get<Even[]>(this.eventSearchUri, {params});
  }

  showSearch(searchTerms: FormGroup, page: number): Observable<Show[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    for (const key in searchTerms.value) {
      if (searchTerms.value[key] !== '') {
        params = params.set(key, searchTerms.value[key]);
      }
    }
    return this.httpClient.get<Show[]>(this.showSearchUri, {params});
  }
}
