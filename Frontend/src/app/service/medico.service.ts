// src/app/services/medico.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Medico } from '../models/medico.model';

@Injectable({ providedIn: 'root' })
export class MedicoService {
  private apiUrl = 'http://localhost:8080/api/medicos';
  private authHeader = {
    headers: new HttpHeaders({
      Authorization: 'Basic ' + btoa('admin@demo.com:admin123'),
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) {}

  obtenerTodos(): Observable<Medico[]> {
    return this.http.get<Medico[]>(this.apiUrl, this.authHeader);
  }

  obtenerPorId(id: number): Observable<Medico> {
    return this.http.get<Medico>(`${this.apiUrl}/${id}`, this.authHeader);
  }

  registrar(medico: any): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/registrar`, medico, this.authHeader);
  }

  actualizar(id: number, medico: any): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/${id}`, medico, this.authHeader);
  }

  eliminar(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`, this.authHeader);
  }
}