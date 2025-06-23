import { Component } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-medico',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgIf,
    NgFor
  ],
  templateUrl: './medico.component.html',
  styleUrls: ['./medico.component.css']
})
export class MedicoComponent {
  seccion: string = 'pacientes';

  pacientes = [
    { nombre: 'Pedro Gómez', edad: 45, cedula: '123456' },
    { nombre: 'Ana Ruiz', edad: 33, cedula: '789012' }
  ];

  mensajePaciente = '';
  pacienteForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.pacienteForm = this.fb.group({
      nombre: ['', Validators.required],
      cedula: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]],
      fechaNacimiento: ['', Validators.required],
      sexo: ['', Validators.required],
      estadoCivil: ['', Validators.required],
      ocupacion: ['', Validators.required],
      actividadFisica: ['', Validators.required],
      peso: [null, Validators.required],
      estatura: [null, Validators.required]
    });
  }

  registrarPaciente(): void {
    const credentials = btoa('admin@demo.com:admin123');
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`,
      'Content-Type': 'application/json'
    });

    this.http.post('http://localhost:8080/api/registro/paciente', this.pacienteForm.value, { headers })
      .subscribe({
        next: () => {
          this.mensajePaciente = '✅ Paciente registrado correctamente';
          this.pacienteForm.reset();
        },
        error: (err) => {
          this.mensajePaciente = '❌ Error al registrar paciente';
          console.error(err);
        }
      });
  }
}