import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule, NgIf, NgForOf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {PacienteComponent} from '../../../components/paciente/paciente.component'; // Ajusta la ruta según tu estructura

export interface Usuario {
  id: number;
  nombre: string;
  cedula: string;
  correo: string;
  contrasena: string;
  fechaNacimiento: string | null;
  rol: string | null;
}

export interface Medico {
  idMedico: number;
  especialidad: string;
  registroProfesional: string;
  user: Usuario;
}

@Component({
  selector: 'app-inicio',
  
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule, NgIf, NgForOf, FormsModule, PacienteComponent],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent implements OnInit {
  medicoForm: FormGroup;
  mensaje: string = '';
  error: boolean = false;
  seccion: string = 'medicos';
  medicos: Medico[] = [];
  editarId: number | null = null;
  busquedaId: number | null = null;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.medicoForm = this.fb.group({
      nombre: ['', Validators.required],
      cedula: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
      correo: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]],
      especialidad: ['', Validators.required],
      registroProfesional: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.obtenerMedicos();
  }

  obtenerMedicos(): void {
    const headers = {
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`
    };

    this.http.get<Medico[]>('http://localhost:8080/api/medicos', { headers }).subscribe({
      next: (data) => this.medicos = data,
      error: (err) => console.error('Error al obtener médicos', err)
    });
  }

  buscarMedicoPorId(): void {
    if (!this.busquedaId) {
      this.mensaje = '❗ Ingresa un ID válido';
      this.error = true;
      return;
    }

    const headers = {
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`
    };

    this.http.get<Medico>(`http://localhost:8080/api/medicos/cedula/${this.busquedaId}`, { headers }).subscribe({
      next: (medico) => {
        this.medicos = [medico];
        this.mensaje = '';
        this.error = false;
      },
      error: () => {
        this.mensaje = '❌ Médico no encontrado';
        this.error = true;
        this.medicos = [];
      }
    });
  }

  editarMedico(medico: Medico): void {
    this.editarId = medico.idMedico;
    this.medicoForm.patchValue({
      nombre: medico.user.nombre,
      cedula: medico.user.cedula,
      correo: medico.user.correo,
      contrasena: '',
      especialidad: medico.especialidad,
      registroProfesional: medico.registroProfesional
    });
  }

  eliminarMedico(id: number): void {
    const headers = {
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`
    };

    this.http.delete(`http://localhost:8080/api/medicos/${id}`, { headers }).subscribe({
      next: () => {
        this.mensaje = '✅ Médico eliminado correctamente';
        this.error = false;
        this.obtenerMedicos();
      },
      error: (err) => {
        this.mensaje = err.error?.message || '❌ Error al eliminar médico';
        this.error = true;
        console.error('Error al eliminar:', err);
      }
    });
  }

  onSubmit(): void {
    const headers = {
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`,
      'Content-Type': 'application/json'
    };

    const medicoPayload = {
      nombre: this.medicoForm.value.nombre,
      cedula: this.medicoForm.value.cedula,
      correo: this.medicoForm.value.correo,
      contrasena: this.medicoForm.value.contrasena,
      especialidad: this.medicoForm.value.especialidad,
      registroProfesional: this.medicoForm.value.registroProfesional
    };

    if (this.editarId) {
      this.http.put(`http://localhost:8080/api/medicos/${this.editarId}`, medicoPayload, { headers }).subscribe({
        next: () => {
          this.mensaje = '✅ Médico actualizado correctamente';
          this.error = false;
          this.editarId = null;
          this.medicoForm.reset();
          this.obtenerMedicos();
        },
        error: (err) => {
          this.mensaje = err.error?.message || '❌ Error al actualizar médico';
          this.error = true;
          console.error('Error al actualizar:', err);
        }
      });
    } else {
      this.http.post('http://localhost:8080/api/registro/medico', medicoPayload, { headers }).subscribe({
        next: () => {
          this.mensaje = '✅ Médico registrado correctamente';
          this.error = false;
          this.medicoForm.reset();
          this.obtenerMedicos();
        },
        error: (err) => {
          this.mensaje = err.error?.message || '❌ Error al registrar médico';
          this.error = true;
          console.error('Error al registrar:', err);
        }
      });
    }
  }
}