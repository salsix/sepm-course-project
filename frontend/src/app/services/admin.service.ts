import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {ApplicationUser} from '../dtos/application-user';
import {FormGroup} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private usersBaseUri: string = this.globals.backendUri + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  /**
   * get all locked users
   */
  getUsers(): Observable<ApplicationUser[]> {
    console.log('getUsers');
    return this.httpClient.get<ApplicationUser[]>(this.globals.backendUri + '/users');
  }

  unlockUser(email: string): Observable<ApplicationUser> {
    console.log('unlock user with email ' + email);
    return this.httpClient.post<ApplicationUser>(this.globals.backendUri + '/users/unlock', email);
  }

  lockUser(email: string): Observable<ApplicationUser> {
    console.log('lock user with email ' + email);
    return this.httpClient.post<ApplicationUser>(this.globals.backendUri + '/users/locked', email);
  }

  resetUserPassword(email: string): Observable<ApplicationUser> {
    console.log('reset password of user with email ' + email);
    return this.httpClient.post<ApplicationUser>(this.globals.backendUri + '/users/resetpassword', email);
  }

  createUser(user: ApplicationUser): Observable<ApplicationUser> {
    console.log(this.usersBaseUri + '/create');
    console.log(user);
    return this.httpClient.post<ApplicationUser>(this.usersBaseUri + '/create', user);
  }

  searchUsers(searchTerms: FormGroup, page: number): Observable<ApplicationUser[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    for (const key in searchTerms.value) {
      if (searchTerms.value[key] !== '') {
        params = params.set(key, searchTerms.value[key]);
      }
    }
    console.log(params);
    return this.httpClient.get<ApplicationUser[]>(this.usersBaseUri, {params});
  }

}
