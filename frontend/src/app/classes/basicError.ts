import {ActivatedRoute, Router} from '@angular/router';
import {Alert} from './alert';

export class BasicError {
  basicAlert: Alert = null;

  constructor(router?: Router) {
    if(!router || !router.getCurrentNavigation() || !router.getCurrentNavigation().extras){
      return;
    }

    const state = router.getCurrentNavigation().extras.state;
    if(!state || !state['alert']) {
      return;
    }

    this.basicAlert = state['alert'];
  }

  public getErrorMessage(error: any): string {
    console.log(error);


    if(error.status===0){
      return 'cannot reach the backend.<br>Check your internet connection!';
    }

    if(error.error==null){
      return '';
    }
    if (typeof error.error === 'object') {
      return  error.error.error;
    } else {
      return  error.error;
    }
  }

  public alertToQuery(alert: Alert): string {
    const s: string[] = [];

    s.push('alert='+alert.type);

    s.push('alert-strong='+alert.strongMessage);

    s.push('alert-message='+alert.message);

    if(alert.duration){
      s.push('alert-duration='+alert.duration);
    }

    return s.join('&');
  }
}
