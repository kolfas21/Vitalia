import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule, NgIf } from '@angular/common';
import { PacienteComponent } from '../../../components/paciente/paciente.component'; // ajusta la ruta según tu estructura

@Component({
  selector: 'app-inicio',
  standalone: true,
  templateUrl: './inicio.component.html', // asegúrate de que coincida con el archivo real
  styleUrls: ['./inicio.component.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgIf,
    PacienteComponent
  ]
})
export class InicioComponent {
  medicoForm: FormGroup;
  mensaje: string = '';
  error: boolean = false;

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

  onSubmit(): void {
    console.log('Formulario enviado:', this.medicoForm.value);

    if (this.medicoForm.invalid) {
      this.medicoForm.markAllAsTouched();
      return;
    }

    this.http.post('http://localhost:8080/api/registro/medico', this.medicoForm.value)
      .subscribe({
        next: () => {
          this.mensaje = '✅ Médico registrado correctamente';
          this.error = false;
          this.medicoForm.reset();
        },
        error: (err) => {
          this.mensaje = err.error?.message || '❌ Ocurrió un error al registrar';
          this.error = true;
          console.error('Error desde el backend:', err);
        }
      });
  }

  campoInvalido(campo: string): boolean {
    const control = this.medicoForm.get(campo);
    return !!(control && control.invalid && control.touched);
  }
}