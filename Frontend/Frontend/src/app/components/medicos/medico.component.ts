import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule, NgIf, NgForOf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { timeout } from 'rxjs/operators';

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
  selector: 'app-medico',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule, NgIf, NgForOf, FormsModule],
  templateUrl: './medico.component.html',
})
export class MedicoComponent implements OnInit {
  medicoForm: FormGroup;
  medicos: Medico[] = [];
  editarId: number | null = null;
  busquedaCedula: string = '';
  cargando: boolean = false;

  // Toast
  toastVisible = false;
  toastMensaje = '';
  toastError = false;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.medicoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      cedula: ['', [Validators.required, Validators.pattern(/^[0-9]{6,12}$/)]],
      correo: ['', [Validators.required, Validators.email]],
      contrasena: ['', []],
      especialidad: ['', Validators.required],
      registroProfesional: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.obtenerMedicos();
  }

  mostrarToast(mensaje: string, esError: boolean = false): void {
    this.toastMensaje = mensaje;
    this.toastError = esError;
    this.toastVisible = true;
    setTimeout(() => this.toastVisible = false, 3500);
  }

  obtenerMedicos(): void {
    this.cargando = true;
    const headers = {
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`
    };

    this.http.get<Medico[]>('http://localhost:8080/api/medicos', { headers }).subscribe({
      next: (data) => {
        this.medicos = data;
      },
      error: () => {
        this.medicos = [];
        this.mostrarToast('❌ Error al obtener médicos', true);
      },
      complete: () => {
        this.cargando = false;
      }
    });
  }

  buscarMedicoPorCedula(): void {
    if (!this.busquedaCedula) {
      this.mostrarToast('❗ Ingresa una cédula válida', true);
      return;
    }

    const headers = {
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`
    };

    this.http.get<Medico>(`http://localhost:8080/api/medicos/cedula/${this.busquedaCedula}`, { headers }).subscribe({
      next: (medico) => {
        this.medicos = [medico];
      },
      error: () => {
        this.mostrarToast('❌ Médico no encontrado', true);
        this.medicos = [];
      }
    });
  }

  editarMedico(medico: Medico): void {
    this.editarId = medico.user.id;
    this.medicoForm.patchValue({
      nombre: medico.user.nombre,
      cedula: medico.user.cedula,
      correo: medico.user.correo,
      contrasena: '',
      especialidad: medico.especialidad,
      registroProfesional: medico.registroProfesional
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelarEdicion(): void {
    this.editarId = null;
    this.medicoForm.reset();
    this.obtenerMedicos();
  }

  eliminarMedico(id: number): void {
    const headers = {
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`
    };

    this.http.delete(`http://localhost:8080/api/medicos/${id}`, { headers }).subscribe({
      next: () => {
        this.mostrarToast('✅ Médico eliminado correctamente');
        this.obtenerMedicos();
      },
      error: (err) => {
        const msg = err.error?.message || '❌ Error al eliminar médico';
        this.mostrarToast(msg, true);
      }
    });
  }

  onSubmit(): void {
    if (this.medicoForm.invalid) {
      this.mostrarToast('❗ Verifica los campos del formulario', true);
      return;
    }

    const headers = {
      Authorization: `Basic ${btoa('admin@demo.com:admin123')}`,
      'Content-Type': 'application/json'
    };

    const payload = {
      nombre: this.medicoForm.value.nombre,
      cedula: this.medicoForm.value.cedula,
      correo: this.medicoForm.value.correo,
      contrasena: this.medicoForm.value.contrasena,
      especialidad: this.medicoForm.value.especialidad,
      registroProfesional: this.medicoForm.value.registroProfesional
    };

    const endpoint = this.editarId
      ? `http://localhost:8080/api/medicos/${this.editarId}`
      : 'http://localhost:8080/api/medicos/registrar';

    const metodo = this.editarId
      ? this.http.put(endpoint, payload, { headers })
      : this.http.post(endpoint, payload, { headers });

    metodo.pipe(timeout(5000)).subscribe({
      next: () => {
        const mensaje = this.editarId ? '✅ Médico actualizado correctamente' : '✅ Médico registrado correctamente';
        this.mostrarToast(mensaje);
        this.medicoForm.reset();
        this.editarId = null;
        this.obtenerMedicos();
      },
      error: (err) => {
        if (err.status === 200 || err.status === 201) {
          this.mostrarToast('⚠️ Operación completada pero sin respuesta.', false);
        } else {
          const msg = err.error?.message || '❌ Error en la operación';
          this.mostrarToast(msg, true);
        }
      }
    });
  }
}