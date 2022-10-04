import {ElementRef, Injectable, ViewChild} from '@angular/core';

export enum AlertType {
  SUCCESS = 'success',
  ERROR = 'error',
  INFO = 'info',
  WARNING = 'warning'
}

export type AlertOptions = {
  duration?: number,
  type?: AlertType,
  persistent?: boolean
}

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  private shown: boolean = false;
  private message: String = "";

  // Default options
  private options: AlertOptions = {
    duration: 5000,
    type: AlertType.INFO,
    persistent: false
  };

  constructor() {  }

  init() {
    this.shown = false;
    this.message = "";
  }

  show(message: String, options?: AlertOptions): void {
    this.message = message;
    this.shown = true;
    this.options = {
      ...this.options,
      ...options
    }
    if (!this.options.persistent) setTimeout(() => this.shown = false, this.options.duration);
  }

  hide() {
    this.shown = false;
  }


  get isShown() {
    return this.shown;
  }

  get getMessage() {
    return this.message;
  }

  get getOptions() {
    return this.options;
  }
}
