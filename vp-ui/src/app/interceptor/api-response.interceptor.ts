import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

import {catchError, Observable, tap, throwError} from 'rxjs';
import {AlertService, AlertType} from "../services/alert/alert.service";
import {LoaderService} from "../services/loader/loader.service";
import {environment} from "../../environments/environment";
import {ApiError} from "../model/api-error.model";

/** Pass undirty request through to the next request handler. */
@Injectable()
export class ApiResponseInterceptor implements HttpInterceptor {

  constructor(
    private _loaderService: LoaderService,
    private _alertService: AlertService
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap(() => this._loaderService.hide()),
      catchError(err => this.handleError(err))
    );
  }

  private handleError(err: HttpErrorResponse) {
    const error = err.error as ApiError;
    if (!environment.production) {
      console.error(error.devMessage);
      console.error(error.httpStatus, error.httpStatusString);
    }

    this._alertService.show(
      error.message,
      { duration: 5000, type: AlertType.ERROR }
    );

    return throwError(() => new Error(err.message));
  }
}
