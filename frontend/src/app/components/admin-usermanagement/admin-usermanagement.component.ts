import { Component, OnInit } from '@angular/core';
import {AdminService} from '../../services/admin.service';
import {ApplicationUser} from '../../dtos/application-user';
import {FormControl, FormGroup} from '@angular/forms';
import {BasicError} from '../../classes/basicError';
import {ActivatedRoute, Router} from '@angular/router';


@Component({
  selector: 'app-admin-usermanagement',
  templateUrl: './admin-usermanagement.component.html',
  styleUrls: ['./admin-usermanagement.component.scss']
})
export class AdminUsermanagementComponent extends BasicError implements OnInit {

  userSearchForm = new FormGroup({
    firstname: new FormControl(''),
    lastname: new FormControl(''),
    email: new FormControl(''),
    admin: new FormControl(''),
    locked: new FormControl(''),
    loginfails: new FormControl(''),
  });
  users: ApplicationUser[] = [];

  pageOld = 0;
  page = 0;
  pageLoad = false;

  constructor(private adminService: AdminService, private route: ActivatedRoute, private router: Router) {
    super(router);
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  public pagination(page: number) {
    this.pageLoad = true;
    this.pageOld = this.page;

    this.page = page - 1;

    if (this.page === this.pageOld) {
      this.pageLoad = false;
      return;
    }

    this.loadUsers();
  }

  public loadUsers() {

    if (this.userSearchForm.value.locked) {
      this.userSearchForm.value.loginFails = 5;
    } else {
      this.userSearchForm.value.locked = '';
      this.userSearchForm.value.loginfails = '';
    }
    if (!this.userSearchForm.value.admin) {
      this.userSearchForm.value.admin = '';
    }

    this.adminService.searchUsers(this.userSearchForm, this.page).subscribe(
      (result: ApplicationUser[]) => {
        if (result.length === 0 && this.users.length > 0) {
          this.basicAlert = {type: 'warning', strongMessage: '', message: 'No more users', duration: 2};
          this.page = this.pageOld;
          this.pageLoad = false;
          return;
        }
        this.users = result;
        this.pageLoad = false;
      },
      error => {
        this.basicAlert = {
          type: 'danger',
          strongMessage: 'Error: ',
          message: this.getErrorMessage(error),
          duration: -1
        };
        this.pageLoad = false;
      }
    );
  }

  unlockUser(email) {
    this.adminService.unlockUser(email).subscribe(() => this.loadUsers());
  }

  lockUser(email) {
    this.adminService.lockUser(email).subscribe(() => this.loadUsers());
  }

  resetUserPassword(email) {
    this.adminService.resetUserPassword(email).subscribe();
    this.basicAlert = {
      type: 'success',
      strongMessage: 'Reset successful: ',
      message: 'The user "' + email + '" will be forced to choose a new password.',
    };
  }
}
