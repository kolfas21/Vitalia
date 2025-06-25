import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  standalone: true,
  selector: 'app-citas',
  templateUrl: './citas.component.html',
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule] // ✅ Soluciona el error
})
export class CitasComponent implements OnInit {
  consultaForm: FormGroup;
  medicos: any[] = [];
  medicosFiltrados: any[] = [];
  especialidades: string[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.consultaForm = this.fb.group({
      especialidad: ['', Validators.required],
      medicoId: ['', Validators.required],
      motivo: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.obtenerMedicos();
  }

  obtenerMedicos() {
    this.http.get<any[]>('http://localhost:8080/api/medicos').subscribe({
      next: (data) => {
        this.medicos = data;
        this.especialidades = [...new Set(data.map(m => m.especialidad))];
      },
      error: (err) => console.error('Error obteniendo médicos:', err)
    });
  }

  filtrarPorEspecialidad() {
    const especialidadSeleccionada = this.consultaForm.get('especialidad')?.value;
    this.medicosFiltrados = this.medicos.filter(m => m.especialidad === especialidadSeleccionada);
  }

  enviarConsulta() {
    if (this.consultaForm.invalid) {
      this.consultaForm.markAllAsTouched();
      return;
    }

    const idUsuario = localStorage.getItem('id');
    if (!idUsuario) {
      alert('⚠️ No hay usuario autenticado.');
      return;
    }

    const consultaDTO = {
      idPaciente: Number(idUsuario),
      idMedico: Number(this.consultaForm.value.medicoId),
      motivo: this.consultaForm.value.motivo
    };

    this.http.post('http://localhost:8080/api/usuarios/consulta', consultaDTO).subscribe({
      next: (res) => {
        alert('✅ Consulta enviada correctamente');
        this.consultaForm.reset();
      },
      error: (err) => {
        console.error('❌ Error al enviar consulta:', err);
        alert('❌ Error al enviar consulta');
      }
    });
  }
}


