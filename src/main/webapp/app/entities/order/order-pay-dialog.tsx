import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, payEntity } from './order.reducer';

export const OrderPayDialog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const orderEntity = useAppSelector(state => state.order.entity);
  const updateSuccess = useAppSelector(state => state.order.updateSuccess);

  const handleClose = () => {
    navigate('/order');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmPay = () => {
    dispatch(payEntity(orderEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="orderPayDialogHeading">
        Confirm pay operation
      </ModalHeader>
      <ModalBody id="bagelApp.order.pay.question">Are you sure you want to pay Order {orderEntity.id}?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-pay-order" data-cy="entityConfirmPayButton" color="info" onClick={confirmPay}>
          <FontAwesomeIcon icon="eye" />
          &nbsp; Pay
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default OrderPayDialog;
