import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {Alert} from '../../classes/alert';
import {BasicError} from '../../classes/basicError';
import {PasswordValidService} from '../../services/password-valid.service';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent extends BasicError implements OnInit {

  registrationForm: FormGroup;
  alert: Alert;
  submitting = false;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private router: Router,
              private customValidator: PasswordValidService) {
    super();
  }

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      firstName: [''],
      lastName: [''],
      email: [''],
      password: [''],
      confirmPassword: [''],
      gender: [''],
      dateOfBirth: [''],
      admin: ['false']
    }, {
      validator: this.customValidator.passwordMatchValidator('password', 'confirmPassword')
    });
  }

  registerUser() {
    this.submitting = true;
    if (this.registrationForm.valid) {
      this.userService.registerUser(this.registrationForm.value).subscribe(
        () => {
          this.submitting = false;
          this.alert = {
            type: 'success',
            strongMessage: 'Success: ',
            message: ' Account is registered, You will be redirected to the login after few seconds'
          };
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 4000);
        },
        error => {
          this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: 8};
          this.submitting = false;
        }
      );
    }
  }

}
