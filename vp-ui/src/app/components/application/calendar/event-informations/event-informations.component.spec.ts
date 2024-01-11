import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventInformationsComponent } from './event-informations.component';

describe('EventInformationsComponent', () => {
  let component: EventInformationsComponent;
  let fixture: ComponentFixture<EventInformationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventInformationsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventInformationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
