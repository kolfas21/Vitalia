import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface Consulta {
  idConsulta: number;
  nombrePaciente: string;
  cedulaPaciente: string;
  nombreMedico: string;
  especialidad: string;
  fechaHora: string;
}

@Component({
  selector: 'app-consultas-medico',
  standalone: true,
  imports: [CommonModule, HttpClientModule, ReactiveFormsModule],
  templateUrl: './citas-medico.html',
})
export class ConsultasMedicoComponent implements OnInit {
  consultas: Consulta[] = [];
  selectedConsulta: Consulta | null = null;
  detalleForm!: FormGroup;
  detalleVisible = false;

  constructor(private http: HttpClient, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.detalleForm = this.fb.group({
      sintomas: [''],
      alergias: [''],
      actividadFisica: [''],
      ocupacion: [''],
      peso: [''],
      estatura: ['']
    });

    const idMedico = localStorage.getItem('idMedico');
    if (idMedico) {
      this.http.get<Consulta[]>(`/api/consulta/medico/${idMedico}`).subscribe({
        next: data => this.consultas = data,
        error: err => alert('Error al obtener consultas')
      });
    }
  }

  verDetalle(idConsulta: number) {
    this.http.get<any>(`/api/consulta/${idConsulta}/detalle`).subscribe({
      next: detalle => {
        this.selectedConsulta = this.consultas.find(c => c.idConsulta === idConsulta) || null;
        this.detalleForm.patchValue({
          sintomas: detalle.sintomas,
          alergias: detalle.alergias,
          actividadFisica: detalle.actividadFisica,
          ocupacion: detalle.ocupacion,
          peso: detalle.peso,
          estatura: detalle.estatura
        });
        this.detalleVisible = true;
      },
      error: err => alert('Error al obtener detalle de consulta')
    });
  }

  actualizarInfoMedica() {
    if (!this.selectedConsulta) return;

    this.http.put(`/api/consulta/${this.selectedConsulta.idConsulta}/paciente`, this.detalleForm.value).subscribe({
      next: () => alert('✅ Información médica actualizada'),
      error: err => alert('Error al actualizar información médica')
    });
  }

  cerrarDetalle() {
    this.selectedConsulta = null;
    this.detalleVisible = false;
  }
}