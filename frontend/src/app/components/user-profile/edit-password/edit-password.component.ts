import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Alert} from '../../../classes/alert';
import {UserService} from '../../../services/user.service';
import {PasswordValidService} from '../../../services/password-valid.service';
import {BasicError} from '../../../classes/basicError';
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-edit-password',
  templateUrl: './edit-password.component.html',
  styleUrls: ['./edit-password.component.scss']
})
export class EditPasswordComponent extends BasicError implements OnInit {

  passwordUpdateForm: FormGroup;
  alert: Alert;
  submitting = false;
  pwreset = false;

  constructor(private authService: AuthService, private formBuilder: FormBuilder, private userService: UserService,
              private customValidator: PasswordValidService) {
    super();
  }

  ngOnInit(): void {
    if (this.authService.getUserRole() === 'PWRESET') {
      this.pwreset = true;
    }
    this.passwordUpdateForm = this.formBuilder.group({
      password: [''],
      confirmPassword: ['']
    }, {
      validator: this.customValidator.passwordMatchValidator('password', 'confirmPassword')
    });
  }

  updatePassword() {
    this.submitting = true;
    if (this.passwordUpdateForm.valid){
      this.userService.updatePassword(this.passwordUpdateForm.value).subscribe(
        () => {
          this.submitting = false;
          this.alert = {type: 'success', strongMessage: 'Success: ', message: 'Your Password was updated successfully'};
        },
        error => {
          this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: 8};
          this.submitting = false;
        }
      );
    }
  }
}
