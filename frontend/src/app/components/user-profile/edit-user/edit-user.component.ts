import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {UserService} from '../../../services/user.service';
import {Router} from '@angular/router';
import {UserUpdate} from '../../../dtos/user-update';
import {Alert} from '../../../classes/alert';
import {BasicError} from '../../../classes/basicError';
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent extends BasicError implements OnInit {

  editUserForm: FormGroup;
  user: UserUpdate;
  alert: Alert;
  submitting = false;

  constructor(private userService: UserService,
              private formBuilder: FormBuilder,
              private router: Router,
              private authService: AuthService) {
    super();
  }

  ngOnInit(): void {
    this.loadUser();
  }

  loadUser() {
    return this.userService.getUser().subscribe(user => {
      this.user = user;
      this.toEditUserForm();
    });
  }

  toEditUserForm() {
    this.editUserForm = this.formBuilder.group({
      firstName: [this.user.firstName],
      lastName: [this.user.lastName],
      email: [this.user.email],
      gender: [this.user.gender],
      dateOfBirth: [this.user.dateOfBirth]
    });
  }


  updateUser() {
    this.submitting = true;
    if (this.editUserForm.valid) {
      this.userService.updateUser(this.editUserForm.value).subscribe(
        () => {
          this.submitting = false;
          this.authService.logoutUser();
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
          this.alert = {
            type: 'success',
            strongMessage: 'Success: ',
            message: ' Account is updated, please login again'
          };
        },
        error => {
          this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: 8};
          this.submitting = false;
        }
      );
    }
  }
}
