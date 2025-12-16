import classNames from "classnames";
import { FiFile } from "react-icons/fi";

type Props = {
    filename: string
    selected: boolean
    onClick: () => void
}

export function FileCard(props: Props) {
    return (
        <div onClick={props.onClick} className={classNames("border-2 rounded cursor-pointer transition-colors", {
            "border-blue-500 shadow": props.selected,
            "border-transparent": !props.selected,
        })}>
            <div className={classNames("h-32 rounded-t flex", {
                "bg-gray-100 text-gray-800": !props.selected,
                "bg-blue-50 text-blue-800": props.selected,
            })}>
                <FiFile size={24} className="m-auto" />
            </div>
            <p className={classNames("w-full text-center text-sm rounded-b py-2 px-3 my-auto", {
                "bg-gray-200 text-gray-700": !props.selected,
                "bg-blue-100 text-blue-700": props.selected,
            })}>{props.filename}</p>
        </div>
    )
}