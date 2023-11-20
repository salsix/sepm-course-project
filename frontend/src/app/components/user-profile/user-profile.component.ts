import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {Alert} from '../../classes/alert';
import {BasicError} from '../../classes/basicError';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent extends BasicError implements OnInit {

  alert: Alert;
  submitting = false;

  constructor(private userService: UserService, private authService: AuthService, private router: Router) {
    super();
  }

  ngOnInit(): void {
  }

  deleteUser() {
    this.submitting = true;
    this.userService.deleteUser().subscribe(
      () => {
        this.submitting = false;
        this.authService.logoutUser();
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 2000);
        this.alert = {type: 'success', strongMessage: 'Success ', message: 'Account deleted'};
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: 8};
        this.submitting = false;
      }
    );
  }

}
