import React, { useEffect, useState } from "react";
import styles from "./ProfilePage.module.scss";
import { getProfile } from "../../../api/profileApi";
import { token } from "../../../utils/token";

export default function ProfilePage() {
    const [profile, setProfile] = useState({
        fullName: "",
        phone: "",
        address: "",
        avatarUrl: "/default-avatar.png",
    });
    const [editing, setEditing] = useState(false);
    const [avatarFile, setAvatarFile] = useState(null);

    useEffect(() => {
        const fetchProfile = async () => {
            const data = await getProfile();
            setProfile(data.profile);
        };
        fetchProfile();
    }, []);

    const handleAvatarChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setAvatarFile(file);
            setProfile({ ...profile, avatarUrl: URL.createObjectURL(file) });
        }
    };

    const handleSave = async () => {
        const formData = new FormData();
        formData.append("fullName", profile.fullName);
        formData.append("phone", profile.phone);
        formData.append("address", profile.address);
        if (avatarFile) formData.append("avatar", avatarFile);

        const data = await fetch("/api/profile", {
            method: "PUT",
            headers: {
                Authorization: `Bearer ${token.get()}`,
            },
            body: formData,
        }).then((res) => res.json());

        setProfile(data.profile);
        setEditing(false);
        setAvatarFile(null);
    };

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Hồ sơ cá nhân</h1>

            <div className={styles.card}>
                <div className={styles.avatarSection}>
                    <img src={profile.avatarUrl} alt="avatar" className={styles.avatar} />
                    {editing && (
                        <label className={styles.avatarEdit}>
                            <input
                                type="file"
                                accept="image/*"
                                onChange={handleAvatarChange}
                            />
                            ✎
                        </label>
                    )}
                </div>

                <div className={styles.row}>
                    <label>Email:</label>
                    <span>{token.getEmail()}</span>
                </div>

                <div className={styles.row}>
                    <label>Họ và tên:</label>
                    {editing ? (
                        <input
                            type="text"
                            value={profile.fullName}
                            onChange={(e) =>
                                setProfile({ ...profile, fullName: e.target.value })
                            }
                        />
                    ) : (
                        <span>{profile.fullName}</span>
                    )}
                </div>

                <div className={styles.row}>
                    <label>Số điện thoại:</label>
                    {editing ? (
                        <input
                            type="text"
                            value={profile.phone}
                            onChange={(e) =>
                                setProfile({ ...profile, phone: e.target.value })
                            }
                        />
                    ) : (
                        <span>{profile.phone}</span>
                    )}
                </div>

                <div className={styles.row}>
                    <label>Địa chỉ:</label>
                    {editing ? (
                        <textarea
                            value={profile.address}
                            onChange={(e) =>
                                setProfile({ ...profile, address: e.target.value })
                            }
                        />
                    ) : (
                        <span>{profile.address}</span>
                    )}
                </div>

                <div className={styles.actions}>
                    {editing ? (
                        <button className={styles.saveBtn} onClick={handleSave}>
                            Lưu
                        </button>
                    ) : (
                        <button
                            className={styles.editBtn}
                            onClick={() => setEditing(true)}
                        >
                            Chỉnh sửa
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}