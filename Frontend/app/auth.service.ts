import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap, BehaviorSubject, catchError } from 'rxjs';
import { User } from './models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private userIdSubject = new BehaviorSubject<number | null>(null);

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password }, { withCredentials: true }).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('token', response.token);
          localStorage.setItem('role', response.role);
          this.userIdSubject.next(response.userId || null);
        }
      }),
      catchError(error => {
        console.error('Login error:', error);
        throw error;
      })
    );
  }
  

register(user: User): Observable<string> {
  return this.http.post(`${this.apiUrl}/register`, user, { responseType: 'text' });
}


  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getUserId(): Observable<number | null> {
    return this.userIdSubject.asObservable();
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.userIdSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  addTokenHeader(request: any): any {
    const token = this.getToken();
    if (token) {
      return request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return request;
  }
}
