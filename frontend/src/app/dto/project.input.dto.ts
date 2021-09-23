import { ProjectManagerInputDTO } from "./projectManager.input.dto";

export interface projectInputDTO {
    projectId: number,
    projectName: string,
    projectDescription: string,
    projectManagers: ProjectManagerInputDTO[];
}