import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Alert} from '../../../classes/alert';
import {Router} from '@angular/router';
import {PasswordValidService} from '../../../services/password-valid.service';
import {BasicError} from '../../../classes/basicError';
import {AdminService} from '../../../services/admin.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent extends BasicError implements OnInit {

  createUserForm: FormGroup;
  alert: Alert;
  submitting = false;

  constructor(private formBuilder: FormBuilder,
              private adminService: AdminService,
              private router: Router,
              private customValidator: PasswordValidService) {
    super();
  }

  ngOnInit(): void {
    this.createUserForm = this.formBuilder.group({
      firstName: [''],
      lastName: [''],
      email: [''],
      password: [''],
      confirmPassword: [''],
      gender: [''],
      dateOfBirth: [''],
      admin: ['']
    }, {
      validator: this.customValidator.passwordMatchValidator('password', 'confirmPassword')
    });
  }

  createUser() {
    this.submitting = true;
    if (this.createUserForm.valid) {
      this.adminService.createUser(this.createUserForm.value).subscribe(
        () => {
          this.submitting = false;
          this.alert = {
            type: 'success',
            strongMessage: 'Success: ',
            message: ' Account is registered'
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
