import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {AdminEventListComponent} from './components/admin-event-list/admin-event-list.component';
import {AdminEventComponent} from './components/admin-event/admin-event.component';
import {AdminShowComponent} from './components/admin-show/admin-show.component';
import {HallplanEditComponent} from './components/hallplan/hallplan-edit/hallplan-edit.component';
import {AdminLocationComponent} from './components/location/admin-location/admin-location.component';
import {AdminLocationListComponent} from './components/location/admin-location-list/admin-location-list.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {AdminUsermanagementComponent} from './components/admin-usermanagement/admin-usermanagement.component';
import {EditUserComponent} from './components/user-profile/edit-user/edit-user.component';
import {UserProfileComponent} from './components/user-profile/user-profile.component';
import {EditPasswordComponent} from './components/user-profile/edit-password/edit-password.component';
import {ShowTicketsComponent} from './components/show-tickets/show-tickets.component';
import {UserTicketsComponent} from './components/user-tickets/user-tickets.component';
import {UserBillComponent} from './components/user-bill/user-bill.component';
import {EventComponent} from './components/event/event.component';
import {CreateUserComponent} from './components/admin-dashboard/create-user/create-user.component';
import {AdminGuard} from './guards/admin.guard';
import {NewsComponent} from './components/news/news/news.component';
import {NewsDetailsComponent} from './components/news/news-details/news-details.component';
import {NewsPreviousComponent} from './components/news/news-previous/news-previous.component';
import {NewsCurrentComponent} from './components/news/news-current/news-current.component';
import {TopTenComponent} from './components/top-ten/top-ten.component';
import {SearchComponent} from './components/search/search.component';
import {AdminDashboardComponent} from './components/admin-dashboard/admin-dashboard.component';
import {PwresetGuard} from './guards/pwreset.guard';

const routes: Routes = [

  // public routes
  {path: '', component: HomeComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'login', component: LoginComponent},

  // user routes
  {path: 'topten', canActivate: [AuthGuard], component: TopTenComponent},
  {path: 'events/:id', canActivate: [AuthGuard], component: EventComponent},
  {path: 'shows/:id', canActivate: [AuthGuard], component: ShowTicketsComponent},
  {path: 'search', canActivate: [AuthGuard], component: SearchComponent},

  {path: 'news', canActivate: [AuthGuard], component: NewsComponent},
  {path: 'news-previous/:id', canActivate: [AuthGuard], component: NewsDetailsComponent},
  {path: 'news-previous', canActivate: [AuthGuard], component: NewsPreviousComponent},
  {path: 'news-current/:id', canActivate: [AuthGuard], component: NewsDetailsComponent},
  {path: 'news-current', canActivate: [AuthGuard], component: NewsCurrentComponent},

  {path: 'user/tickets', canActivate: [AuthGuard], component: UserTicketsComponent},
  {path: 'user/tickets/:id', canActivate: [AuthGuard], component: UserBillComponent},

  {path: 'user/settings/edit', canActivate: [AuthGuard], component: EditUserComponent},
  {path: 'user/settings', canActivate: [AuthGuard], component: UserProfileComponent},
  {path: 'user/settings/password', canActivate: [PwresetGuard], component: EditPasswordComponent},

  // admin routes
  {path: 'admin/events/:id/shows/:id', canActivate: [AdminGuard], component: AdminShowComponent},
  {path: 'admin/events/:id', canActivate: [AdminGuard], component: AdminEventComponent},
  {path: 'admin/events', canActivate: [AdminGuard], component: AdminEventListComponent},

  {path: 'admin/locations/:plan/hallplans/:id', canActivate: [AdminGuard], component: HallplanEditComponent},
  {path: 'admin/locations/:id', canActivate: [AdminGuard], component: AdminLocationComponent},
  {path: 'admin/locations', canActivate: [AdminGuard], component: AdminLocationListComponent},

  {path: 'admin/users', canActivate: [AdminGuard], component: AdminUsermanagementComponent},
  {path: 'admin/create', canActivate: [AdminGuard], component: CreateUserComponent},
  {path: 'admin/dashboard', canActivate: [AdminGuard], component: AdminDashboardComponent},
  {path: 'admin/create', canActivate: [AdminGuard], component: CreateUserComponent},

  {path: '**', redirectTo: '/news-current', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
