import { GetUserStoriesInputDTO } from "./getUserStoriesInputDTO";

export interface GetSprintsInputDTO {
    id: number,
    name: string,
    startDate: string,
    endDate: string,
    userStories: GetUserStoriesInputDTO[]
}