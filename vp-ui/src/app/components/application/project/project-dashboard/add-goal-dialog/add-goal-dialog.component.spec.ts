import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddGoalDialogComponent } from './add-goal-dialog.component';

describe('AddGoalDialogComponent', () => {
  let component: AddGoalDialogComponent;
  let fixture: ComponentFixture<AddGoalDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddGoalDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddGoalDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
