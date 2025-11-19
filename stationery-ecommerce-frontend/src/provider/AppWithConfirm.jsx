import { ConfirmProvider } from "react-confirm";

export default function AppWithConfirm({ children }) {
    return <ConfirmProvider>{children}</ConfirmProvider>;
}
