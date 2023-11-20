import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthService, public route: ActivatedRoute) { }

  ngOnInit() {
    console.log(this.route, this.route.toString());
  }


  ownFunc() {
    console.log(this.route, this.route.toString());
  }

}
