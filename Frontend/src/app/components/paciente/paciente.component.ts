import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { CommonModule, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-paciente',
  standalone: true,
imports: [CommonModule, ReactiveFormsModule, FormsModule, HttpClientModule, NgForOf], 
 templateUrl: './paciente.component.html',
})
export class PacienteComponent implements OnInit {
  pacienteForm: FormGroup;
  pacientes: any[] = [];
  cedulaBusqueda: string = '';
  apiUrl = 'http://localhost:8080/api/usuarios';
  editando = false; // se mantiene, pero no se usa por ahora
  idEditando: number | null = null;

  headers = {
    headers: new HttpHeaders({
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`
    })
  };

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.pacienteForm = this.fb.group({
      nombre: ['', Validators.required],
      cedula: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      fechaNacimiento: [''],
      rol: ['PACIENTE'],
    });
  }

  ngOnInit(): void {
    this.obtenerPacientes();
  }

  obtenerPacientes() {
    this.http.get<any[]>(this.apiUrl, this.headers).subscribe(data => {
      this.pacientes = data.filter(p => p.rol === 'PACIENTE');
    });
  }

  buscarPorCedula() {
    const cedula = this.cedulaBusqueda.trim();
    if (!cedula) return;

    this.http.get<any>(`${this.apiUrl}/obtenerPacientePorCedula/${cedula}`, this.headers).subscribe({
      next: paciente => {
        if (paciente.rol === 'PACIENTE') {
          this.pacientes = [paciente];
        } else {
          alert('⚠️ No es un paciente.');
        }
      },
      error: () => {
        alert('❌ Paciente no encontrado');
        this.obtenerPacientes();
      }
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar este paciente?')) {
      this.http.delete(`${this.apiUrl}/${id}`, this.headers).subscribe(() => {
        this.obtenerPacientes();
      });
    }
  }
}