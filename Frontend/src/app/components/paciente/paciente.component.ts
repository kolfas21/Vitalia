import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { CommonModule, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { config } from '../../../config';

@Component({
  selector: 'app-paciente',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, HttpClientModule, NgForOf, NgIf],
  templateUrl: './paciente.component.html',
})
export class PacienteComponent implements OnInit {
  pacientes: any[] = [];
  cedulaBusqueda: string = '';
  apiUrl = `${config.apiUrl}/api/usuarios`;
  mensaje: string = '';
  error: string = '';

  headers = {
    headers: new HttpHeaders({
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`
    })
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.obtenerPacientes();
  }

  obtenerPacientes() {
    this.http.get<any[]>(this.apiUrl, this.headers).subscribe({
      next: data => {
        this.pacientes = data
          .filter(p => p.rol === 'PACIENTE')
          .map(p => ({
            id: p.id,
            nombre: p.nombre,
            cedula: p.cedula,
            correo: p.correo,
            fechaNacimiento: p.fechaNacimiento,
            rol: p.rol
          }));
        this.mensaje = '';
        this.error = '';
      },
      error: () => this.error = '❌ Error al obtener los pacientes'
    });
  }

  buscarPorCedula() {
    const cedula = this.cedulaBusqueda.trim();
    if (!cedula) return;

    this.http.get<any>(`${this.apiUrl}/cedula/${cedula}`, this.headers).subscribe({
      next: paciente => {
        if (paciente.rol === 'PACIENTE') {
          this.pacientes = [{
            id: paciente.id,
            nombre: paciente.nombre,
            cedula: paciente.cedula,
            correo: paciente.correo,
            fechaNacimiento: paciente.fechaNacimiento,
            rol: paciente.rol
          }];
          this.mensaje = '';
          this.error = '';
        } else {
          this.error = '⚠️ El usuario no es un paciente';
          this.obtenerPacientes();
        }
      },
      error: () => {
        this.error = '❌ Paciente no encontrado';
        this.obtenerPacientes();
      }
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar este paciente?')) {
      this.http.delete(`${this.apiUrl}/eliminar/${id}`, this.headers).subscribe({
        next: () => {
          this.mensaje = '✅ Paciente eliminado correctamente';
          this.error = '';
          this.obtenerPacientes();
        },
        error: err => {
          this.error = err.error?.error || '❌ Error al eliminar paciente';
        }
      });
    }
  }
}