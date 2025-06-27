import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CdkDragDrop, DragDropModule, transferArrayItem } from '@angular/cdk/drag-drop';
import { config } from '../../../config';

interface Consulta {
  idConsulta: number;
  nombrePaciente: string;
  nombre: string;
  cedula: string;
  correo: string;
  fechaNacimiento: string;
  nombreMedico: string;
  especialidad: string;
  fechaConsulta: string;
  motivo?: string;
  sintomas?: string | null;
  alergias?: string | null;
  actividadFisica?: string | null;
  ocupacion?: string | null;
  peso?: number | null;
  estatura?: number | null;
  diagnostico?: string;
  estado?: 'pendiente' | 'realizada';
  genero?: string;
}

@Component({
  selector: 'app-consultas-medico',
  standalone: true,
  imports: [CommonModule, HttpClientModule, ReactiveFormsModule, FormsModule, DragDropModule],
  templateUrl: './citas-medico.html',
})
export class ConsultasMedicoComponent implements OnInit {
  todasConsultas: Consulta[] = [];
  pendientes: Consulta[] = [];
  realizadas: Consulta[] = [];
  selectedConsulta: Consulta | null = null;
  detalleForm: FormGroup;
  detalleVisible = false;
  selectedDate: string;
  today = new Date().toISOString().substring(0, 10);

  // Diagnóstico
  sintomasParaDiagnostico = '';
  diagnosticoGenerado = '';
  mostrarDiagnostico = false;
  generandoDiagnostico = false;
  diagnosticoCompleto: any = null;

  constructor(private http: HttpClient, private fb: FormBuilder) {
    this.selectedDate = this.today;
    this.detalleForm = this.fb.group({
      sintomas: [''],
      alergias: [''],
      actividadFisica: [''],
      ocupacion: [''],
      peso: [''],
      estatura: ['']
    });
  }

  ngOnInit(): void {
    this.cargarConsultas();
  }

  get puedeGenerarDiagnostico(): boolean {
    return !!(this.selectedConsulta?.sintomas?.length);
  }

  cargarConsultas(): void {
    const idMedico = localStorage.getItem('id');
    if (idMedico) {
      this.http.get<Consulta[]>(`${config.apiUrl}/api/consulta/medico/${idMedico}`).subscribe({
        next: data => {
          this.todasConsultas = data.map(c => ({ 
            ...c, 
            estado: c.estado || 'pendiente',
            fechaConsulta: this.formatearFecha(c.fechaConsulta)
          }));
          this.actualizarConsultasDelDia();
        },
        error: err => console.error('Error al obtener consultas', err)
      });
    }
  }

  formatearFecha(fecha: string): string {
    return new Date(fecha).toISOString().substring(0, 10);
  }

  actualizarConsultasDelDia(): void {
    const consultasFechaSeleccionada = this.todasConsultas.filter(
      c => c.fechaConsulta.startsWith(this.selectedDate)
    );
    
    this.pendientes = consultasFechaSeleccionada.filter(c => c.estado === 'pendiente');
    this.realizadas = consultasFechaSeleccionada.filter(c => c.estado === 'realizada');
  }

  verDetalle(idConsulta: number): void {
    this.http.get<any>(`${config.apiUrl}/api/consulta/${idConsulta}/detalle`).subscribe({
      next: (detalle) => {
        this.selectedConsulta = {
          ...this.todasConsultas.find(c => c.idConsulta === idConsulta),
          ...detalle
        } as Consulta;
        
        this.detalleForm.patchValue({
          sintomas: detalle.sintomas || '',
          alergias: detalle.alergias || '',
          actividadFisica: detalle.actividadFisica || '',
          ocupacion: detalle.ocupacion || '',
          peso: detalle.peso || null,
          estatura: detalle.estatura || null,
        });

        this.sintomasParaDiagnostico = detalle.sintomas || '';
        this.diagnosticoGenerado = detalle.diagnostico || '';
        this.diagnosticoCompleto = null;
        this.mostrarDiagnostico = !!detalle.diagnostico;

        this.detalleVisible = true;
      },
      error: err => console.error('Error al obtener detalle', err)
    });
  }

  actualizarInfoMedica(): void {
    if (!this.selectedConsulta) return;

    const datosActualizados = { ...this.detalleForm.value };

    this.http.put(
      `${config.apiUrl}/api/consulta/${this.selectedConsulta.idConsulta}/paciente`,
      datosActualizados
    ).subscribe({
      next: () => {
        const consultaIndex = this.todasConsultas.findIndex(
          c => c.idConsulta === this.selectedConsulta?.idConsulta
        );

        if (consultaIndex !== -1) {
          this.todasConsultas[consultaIndex] = {
            ...this.todasConsultas[consultaIndex],
            estado: 'realizada',
            ...datosActualizados
          };
          this.actualizarConsultasDelDia();
        }

        this.selectedConsulta = {
          ...this.selectedConsulta,
          ...datosActualizados,
          estado: 'realizada',
          sintomas: datosActualizados.sintomas || ''
        };

        this.sintomasParaDiagnostico = datosActualizados.sintomas || '';
      },
      error: err => console.error('Error al guardar información médica', err)
    });
  }

  generarDiagnostico(): void {
    if (!this.selectedConsulta) return;

    this.generandoDiagnostico = true;

    const sintomasRaw = this.selectedConsulta.sintomas || '';
    const sintomasArray = sintomasRaw
      .split(',')
      .map(s => s.trim())
      .filter(s => s.length > 0);

    const fechaNacimiento = new Date(this.selectedConsulta.fechaNacimiento);
    const hoy = new Date();
    let edad = hoy.getFullYear() - fechaNacimiento.getFullYear();
    const mes = hoy.getMonth() - fechaNacimiento.getMonth();
    if (mes < 0 || (mes === 0 && hoy.getDate() < fechaNacimiento.getDate())) edad--;

    const datosDiagnostico = {
      idConsulta: this.selectedConsulta.idConsulta,
      id: this.selectedConsulta.cedula,
      nombre: this.selectedConsulta.nombrePaciente,
      edad: edad,
      genero: this.selectedConsulta.genero || 'No especificado',
      sintomas: sintomasArray
    };

    this.http.post<any>(
      `${config.apiUrl}/diagnostico/generar`,
      datosDiagnostico
    ).subscribe({
      next: (response) => {
        const data = response.data || {};
        this.diagnosticoCompleto = data.diagnosticos || null;
        this.diagnosticoGenerado = this.diagnosticoCompleto?.condicion || 'Diagnóstico generado exitosamente';

        this.mostrarDiagnostico = true;
        this.generandoDiagnostico = false;

        if (this.selectedConsulta) {
          this.selectedConsulta.diagnostico = this.diagnosticoGenerado;
        }
this.diagnosticoCompleto = response.data.diagnosticos;
        this.actualizarDiagnosticoEnBackend();
      },
      error: err => {
        console.error('Error al generar diagnóstico', err);
        this.diagnosticoGenerado = 'Error al generar el diagnóstico. Por favor intente nuevamente.';
        this.mostrarDiagnostico = true;
        this.generandoDiagnostico = false;
      }
    });
  }

  actualizarDiagnosticoEnBackend(): void {
    if (!this.selectedConsulta) return;

    this.http.put(
      `${config.apiUrl}/api/consulta/${this.selectedConsulta.idConsulta}/diagnostico`,
      { diagnostico: this.diagnosticoGenerado }
    ).subscribe({
      next: () => {
        const consultaIndex = this.todasConsultas.findIndex(
          c => c.idConsulta === this.selectedConsulta?.idConsulta
        );
        
        if (consultaIndex !== -1) {
          this.todasConsultas[consultaIndex] = {
            ...this.todasConsultas[consultaIndex],
            diagnostico: this.diagnosticoGenerado
          };
          this.actualizarConsultasDelDia();
        }
      },
      error: err => console.error('Error al guardar diagnóstico', err)
    });
  }

  cerrarDetalle(): void {
    this.selectedConsulta = null;
    this.detalleVisible = false;
    this.mostrarDiagnostico = false;
    this.diagnosticoCompleto = null;
    this.sintomasParaDiagnostico = '';
    this.diagnosticoGenerado = '';
    this.generandoDiagnostico = false;
    this.detalleForm.reset();
    
  }

  moverConsulta(event: CdkDragDrop<Consulta[]>): void {
    if (event.previousContainer === event.container) return;

    const item = event.previousContainer.data[event.previousIndex];
    const nuevoEstado = event.container.id === 'realizadas' ? 'realizada' : 'pendiente';

    this.http.patch(
      `${config.apiUrl}/api/consulta/${item.idConsulta}/estado`,
      { estado: nuevoEstado }
    ).subscribe({
      next: () => {
        item.estado = nuevoEstado;
        transferArrayItem(
          event.previousContainer.data,
          event.container.data,
          event.previousIndex,
          event.currentIndex
        );
      },
      error: err => console.error('Error al actualizar estado de consulta', err)
    });
  }

  truncateText(text: string = '', limit: number = 50): string {
    if (!text) return '';
    return text.length > limit ? text.substring(0, limit) + '...' : text;
  }

  calcularEdad(fechaNacimiento: string): number {
    const fecha = new Date(fechaNacimiento);
    const hoy = new Date();
    let edad = hoy.getFullYear() - fecha.getFullYear();
    const mes = hoy.getMonth() - fecha.getMonth();
    if (mes < 0 || (mes === 0 && hoy.getDate() < fecha.getDate())) edad--;
    return edad;
  }
}