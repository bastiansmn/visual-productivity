import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import Event from "../../model/event.model";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(
    private http: HttpClient
  ) { }

  getEventsFromProject(projectId: string, from: Date, to: Date): Observable<Event[]> {
    let params = new HttpParams();
    params = params.appendAll({
      projectId: projectId,
      from: from.toLocaleString(),
      to: to.toLocaleString()
    });

    return this.http.get<Event[]>("/api/v1/event/allOfProject", {params: params});
  }

  getMyEvents(from: Date, to: Date): Observable<Event[]> {
    let params = new HttpParams();
    params = params.appendAll({
      from: from.toISOString(),
      to: to.toISOString()
    });

    return this.http.get<Event[]>("/api/v1/event/myEvents", {params: params});
  }

  participate(event: Event): Observable<Event> {
    return this.http.put<Event>("/api/v1/event/participate?event_id=" + event.event_id, null);
  }

  unparticipate(event: Event): Observable<Event> {
    return this.http.put<Event>("/api/v1/event/unparticipate?event_id=" + event.event_id, null);
  }

  create(event: Event, project_id: string): Observable<Event> {
    return this.http.post<Event>("/api/v1/event/create", {
      project_id: project_id,
      name: event.name,
      description: event.description,
      videoCallLink: event.videoCallLink,
      date_start: event.date_start,
      date_end: event.date_end,
      whole_day: event.whole_day
    });
  }

  delete(event: Event): Observable<void> {
    return this.http.delete<void>("/api/v1/event/delete?event_id=" + event.event_id);
  }
}
