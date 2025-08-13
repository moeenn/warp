import { produce } from "immer"

export type State = {
    selectedFile: string | null
}

export type Action =
    | { type: "toggle-selected-file", filename: string }


export const initialState: State = {
    selectedFile: null,
}

export function reducer(state: State, action: Action): State {
    switch (action.type) {
        case "toggle-selected-file":
            return produce(state, draft => {
                if (draft.selectedFile === action.filename) {
                    draft.selectedFile = null;
                } else {
                    draft.selectedFile = action.filename
                }
            })
    }
}