import {Error} from "./model/error.model";
import {environment} from "../environments/environment";
import {AlertType} from "./services/alert/alert.service";
import {EMPTY} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

export const handleError = (err: HttpErrorResponse, services: any) => {
  const error = err.error as Error;
  if (!environment.production) {
    console.error(error.devMessage);
    console.error(error.httpStatus, error.httpStatusString);
  }

  if (!services) return EMPTY;

  if (services.hasOwnProperty('alertService'))
    services.alertService.show(
      error.message,
      { duration: 5000, type: AlertType.ERROR }
    );

  if (services.hasOwnProperty('alertService-persist'))
    services.alertService.show(
      error.message,
      { type: AlertType.ERROR, persist: true }
    );

  if (services.hasOwnProperty('loaderService'))
    services.loaderService.hide();

  if (services.hasOwnProperty('cookieService'))
    services.cookieService.deleteAll();

  if (services.hasOwnProperty('router'))
    services.router.navigate(['/discover']);

  return EMPTY;
}
