export interface CreateTaskOutputDTO {
    userId?: number,
    userStoryId: number,
    description: string,
    duration?: number,
    definitionOfDone?: string,
    dependenciesIDs?: number[]
}