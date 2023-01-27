import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLabelDialogComponent } from './add-label-dialog.component';

describe('AddLabelDialogComponent', () => {
  let component: AddLabelDialogComponent;
  let fixture: ComponentFixture<AddLabelDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddLabelDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddLabelDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
