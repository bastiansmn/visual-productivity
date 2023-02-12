import {Directive, EventEmitter, HostBinding, HostListener, Output} from '@angular/core';

@Directive({
  selector: '[vpFileUpload]'
})
export class FileUploadDirective {

  @Output() fileUploaded: EventEmitter<File> = new EventEmitter();

  @HostBinding("style.background") private background = "#eee";

  constructor() { }

  @HostListener("dragover", ["$event"]) public onDragOver(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.background = "#beb8ff";
  }

  @HostListener("dragleave", ["$event"]) public onDragLeave(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.background = "#eee";
  }

  @HostListener('drop', ['$event']) public onDrop(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.background = '#eee';

    const file: File | undefined = evt.dataTransfer?.files[0];
    if (!file) return;
    this.fileUploaded.emit(file)
  }

}
