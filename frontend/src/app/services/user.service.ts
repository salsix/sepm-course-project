import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserUpdate} from '../dtos/user-update';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {UpdatePassword} from '../dtos/update-password';
import {ApplicationUser} from '../dtos/application-user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * register a new user
   */
  registerUser(user: ApplicationUser): Observable<ApplicationUser> {
    console.log(user);
    console.log(this.userBaseUri + '/register');
    return this.httpClient.post<ApplicationUser>(this.userBaseUri + '/register', user);
  }


  /**
   * Update specific user
   *
   * @param user to add
   */
  updateUser(user: UserUpdate): Observable<UserUpdate> {
    console.log('Update new user ', user);
    return this.httpClient.put<UserUpdate>(
      this.userBaseUri + '/edit',
      user
    );
  }

  /**
   * Get specific user
   *
   */
  getUser(): Observable<UserUpdate> {
    console.log('Load User details');
    console.log(this.userBaseUri + '/userdata');
    return this.httpClient.get<UserUpdate>(this.userBaseUri + '/userdata');
  }

  /**
   * Update password of a specific user
   *
   * @param password of user to be updated
   */
  updatePassword(password: UpdatePassword): Observable<UpdatePassword> {
    console.log('Update password ', password);
    return this.httpClient.put<UpdatePassword>(
      this.userBaseUri + '/password',
      password
    );
  }
  /**
   * Delete specific user account
   *
   */
  deleteUser(): Observable<ApplicationUser> {
    console.log('Delete user');
    return this.httpClient.delete<ApplicationUser>(this.userBaseUri);
  }




}
