import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private username = 'admin@example.com'; // Cambia por el usuario real
  private password = 'admin123'; // Cambia por la contraseña real

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authToken = btoa(`${this.username}:${this.password}`);
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Basic ${authToken}`
      }
    });
    return next.handle(authReq);
  }
}