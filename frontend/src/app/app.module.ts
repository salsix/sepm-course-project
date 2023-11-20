import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import { HallplanEditComponent } from './components/hallplan/hallplan-edit/hallplan-edit.component';
import { HallplanSeatComponent } from './components/hallplan/hallplan-seat/hallplan-seat.component';
import { HallplanPolygonComponent } from './components/hallplan/hallplan-polygon/hallplan-polygon.component';
import { HallplanPolygonTextComponent } from './components/hallplan/hallplan-polygon-text/hallplan-polygon-text.component';
import { AdminLocationComponent } from './components/location/admin-location/admin-location.component';
import { AdminLocationListComponent } from './components/location/admin-location-list/admin-location-list.component';
import { AdminLocationEditComponent } from './components/location/admin-location-edit/admin-location-edit.component';
import { HallplanComponent } from './components/hallplan/hallplan/hallplan.component';
import { HallplanTicketComponent } from './components/hallplan/hallplan-ticket/hallplan-ticket.component';
import { AlertComponent } from './components/alert/alert.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { RegistrationComponent } from './components/registration/registration.component';
import { AdminUsermanagementComponent } from './components/admin-usermanagement/admin-usermanagement.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { EditUserComponent } from './components/user-profile/edit-user/edit-user.component';
import { EditPasswordComponent } from './components/user-profile/edit-password/edit-password.component';
import { AdminEventListComponent } from './components/admin-event-list/admin-event-list.component';
import { AdminEventComponent } from './components/admin-event/admin-event.component';
import { AdminShowComponent } from './components/admin-show/admin-show.component';
import { AdminEventEditComponent } from './components/admin-event-edit/admin-event-edit.component';
import { AdminShowEditComponent } from './components/admin-show-edit/admin-show-edit.component';
import { ShowTicketsComponent } from './components/show-tickets/show-tickets.component';
import { UserTicketsComponent } from './components/user-tickets/user-tickets.component';
import { UserBillComponent } from './components/user-bill/user-bill.component';
import { EventComponent } from './components/event/event.component';
import {NewsComponent} from './components/news/news/news.component';
import {NewsCurrentComponent} from './components/news/news-current/news-current.component';
import {NewsDetailsComponent} from './components/news/news-details/news-details.component';
import {NewsPreviousComponent} from './components/news/news-previous/news-previous.component';
import { SearchComponent } from './components/search/search.component';
import { EventSearchComponent } from './components/search/event-search/event-search.component';
import {AdminDashboardComponent} from './components/admin-dashboard/admin-dashboard.component';
import {CreateUserComponent} from './components/admin-dashboard/create-user/create-user.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { TopTenComponent } from './components/top-ten/top-ten.component';
import { ShowSearchComponent } from './components/search/show-search/show-search.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    AdminEventListComponent,
    AdminEventComponent,
    AdminShowComponent,
    AdminEventEditComponent,
    AdminShowEditComponent,
    HallplanEditComponent,
    HallplanSeatComponent,
    HallplanPolygonComponent,
    HallplanPolygonTextComponent,
    AdminLocationComponent,
    AdminLocationListComponent,
    AdminLocationEditComponent,
    HallplanComponent,
    HallplanTicketComponent,
    AlertComponent,
    RegistrationComponent,
    AdminUsermanagementComponent,
    UserProfileComponent,
    EditUserComponent,
    EditPasswordComponent,
    ShowTicketsComponent,
    UserTicketsComponent,
    UserBillComponent,
    EventComponent,
    NewsComponent,
    NewsCurrentComponent,
    NewsDetailsComponent,
    NewsPreviousComponent,
    PaginationComponent,
    TopTenComponent,
    CreateUserComponent,
    SearchComponent,
    EventSearchComponent,
    NewsPreviousComponent,
    AdminDashboardComponent,
    CreateUserComponent,
    PaginationComponent,
    ShowSearchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
